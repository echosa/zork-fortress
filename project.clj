(defproject zork-fortress "0.1.0-SNAPSHOT"
  :description "A text world-building adventure!"
  :url "http://echosa.github.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.typed "0.2.63"]]
  :plugins [[lein-cloverage "1.0.2"]
            [lein-typed "0.3.5"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :core.typed {:check [zork-fortress.core]}
  :main ^:skip-aot zork-fortress.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
