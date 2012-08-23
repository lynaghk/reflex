Reflex
======

Reflex is a ClojureScript library for automatic state propagation.
Intrinsic application state lives in atoms, from which derived values can be computed using Reflex-provided "computed observables" (COs).
COs always reflect the latest state, can depend on multiple atoms, and are themselves watchable (i.e., you can chain them).
COs are lazily evaluated, computing state only when they are dereferenced---they will notify their watches when they become dirty, but will not pass any values.

```clojure
(def a (atom 1))
(def b (computed-observable (inc @a)))
@b ;;=> 2
(reset! a 2)
@b ;;=> 3
```

To use from ClojureScript add this to your `project.clj`:

    [com.keminglabs/reflex "0.1.1"]


Implementation
==============

Reflex monitors dereferencing of ClojureScript atoms to determine the atoms that each computed observerable depends on.
When any of these atoms change, the dependent CO is marked as dirty.
COs are lazy; their values will be calculated only when the CO is dereferenced (and only then if it dirty; otherwise the cached value is used).

Dependencies are recalculated every time the CO function is run, which means that dependencies can vary.
E.g., if atom `a` is truthy, then computed observable

```clojure
(computed-observable (if @a @b @c))
```

will depend only on `a` and `b`.
If, later, `a` becomes falsey, then the CO would be updated to depend only on `a` and `c`.


Thanks
======

Big hat tip to Knockout.js folks.
Not only did they write great code, they also took the time to write illuminating technical prose!

+ http://knockoutjs.com/documentation/computedObservables.html
+ https://github.com/mbest/knockout-deferred-updates
