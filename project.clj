(defproject com.keminglabs/reflex "0.1.2-SNAPSHOT"
  :description "ClojureScript state propagation."
  :license {:name "BSD" :url "http://www.opensource.org/licenses/BSD-3-Clause"}

  :dependencies [[org.clojure/clojure "1.4.0"]]

  :min-lein-version "2.0.0"

  :plugins [[lein-cljsbuild "0.2.5"]]

  :source-paths ["src/clj" "src/cljs"]

  :cljsbuild {:builds {:test {:source-path "test/integration"
                              :compiler {:output-to "out/test/integration.js"
                                         :optimizations :whitespace
                                         :pretty-print true}}}

              :test-commands {"integration" ["phantomjs"
                                             "test/integration/runner.coffee"]}})
