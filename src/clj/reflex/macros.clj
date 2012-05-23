(ns reflex.macros)

(defmacro capture-derefed [& body]
  `(do
     (reflex.core/reset-deref-watcher!)
     (let [res# (do ~@body)]
       {:res res# :derefed (reflex.core/recently-derefed)})))

(defmacro computed-observable [& body]
  `(let [co# (reflex.core/ComputedObservable. nil true (fn [] ~@body) (gensym "computed-observable") {} {})]
     @co# ;;initial deref to setup watchers
     co#))
