(defproject zork-fortress "0.0.2-SNAPSHOT"
  :description "A text world-building adventure!"
  :url "http://echosa.github.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :plugins [[lein-cloverage "1.0.9"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :main ^:skip-aot zork-fortress.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
