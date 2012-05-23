(ns reflex.core
  (:use-macros [reflex.macros :only [capture-derefed]]))

(def !recently-derefed
  (atom #{} :meta {:no-deref-monitor true}))

(defn notify-deref-watcher! [derefable]
  (when-not (:no-deref-monitor (meta derefable))
    (swap! !recently-derefed #(conj % derefable))))

(defn reset-deref-watcher! []
  (reset! !recently-derefed #{}))

(defn recently-derefed []
  @!recently-derefed)

;;Have atoms make a note when they're dereferenced.
(extend-type Atom
  IDeref
  (-deref [this]
    (notify-deref-watcher! this)
    (.-state this)))


(defprotocol IDisposable
  (dispose! [this]))


;;Similar to Knockout.js's "computed observable".
(defrecord ComputedObservable [state dirty? f key parent-watchables watches]
  IWatchable
  (-notify-watches [this _ _]
    (doseq [[key wf] watches]
      (wf)))
  (-add-watch [this key wf]
    (set! (.-watches this) (assoc watches key wf)))
  (-remove-watch [this key]
    (set! (.-watches this) (dissoc watches key)))

  IDeref
  (-deref [this]
    (notify-deref-watcher! this)
    (if-not dirty?
      (.-state this)
      (let [{:keys [res derefed]} (capture-derefed (f))]
        ;;Update watches on parent atoms.
        ;;This is necessary on each deref because the CO fn is arbitrary and can reference different atoms.
        ;;E.g., (if @a (... @b) (... @c))
        (doseq [w parent-watchables]
          (remove-watch w key))
        
        (set! (.-parent-watchables this) derefed)
        
        (doseq [w derefed]
          (add-watch w key (fn []
                             (set! (.-dirty? this) true)
                             ;;Notify watches that this CO is dirty.
                             (-notify-watches this nil nil))))

        ;;Update state
        (set! (.-state this) res)
        (set! (.-dirty? this) false)
        
        ;;Return newly calculated state.
        res)))

  IDisposable
  (dispose! [this]
    (doseq [w parent-watchables]
      (remove-watch w key))

    ;;is this necessary for GC?
    (set! (.-watches this) nil)))
