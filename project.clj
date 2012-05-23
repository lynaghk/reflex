(defproject com.keminglabs/reflex "0.1.0-SNAPSHOT"
  :description "ClojureScript state propagation."
  :license {:name "BSD" :url "http://www.opensource.org/licenses/BSD-3-Clause"}
    
  :dependencies [[org.clojure/clojure "1.4.0"]]

  :min-lein-version "2.0.0"

  :plugins [[lein-cljsbuild "0.1.10"]]

  :source-paths ["src/clj" "src/cljs"]

  :cljsbuild {:builds {:test {:source-path "test/cljs"
                              :compiler {:output-to "out/test/integration.js"
                                         :optimizations :simple
                                         :pretty-print true}}}})
