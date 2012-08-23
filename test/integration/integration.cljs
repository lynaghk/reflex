(ns test.integration.integration
  (:use-macros [reflex.macros :only [computed-observable constrain!]])
  ;;need to explicitly require since macros reference this ns
  (:require [reflex.core :as reflex]))


(let [!counter (atom 0)
      !signal (atom "All I do is change")]

  (constrain!
   ;;when I change...
   @!signal
   ;;update the counter
   (swap! !counter inc))

  (assert (= 1 @!counter)
          "Constraint run on init")

  (reset! !signal "foo")

  (assert (= 2 @!counter)
          "Counter auto updated"))



(let [!x (atom 0)
      !co (computed-observable (inc @!x))]

  (assert (= 1 @!co)
          "CO has correct value on first deref")

  (swap! !x inc)

  (assert (= 2 @!co)
          "CO auto-updates"))





(.log js/console "Hurray, all tests passed!")

