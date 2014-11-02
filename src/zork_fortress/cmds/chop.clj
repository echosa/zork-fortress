(ns zork-fortress.cmds.chop
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]))

(t/ann no-trees-msg String)
(def no-trees-msg
  "There are no trees to chop.")

(t/ann chop-cmd [t2/Game -> String])
(defn chop-cmd
  "The chop command."
  [game]
  no-trees-msg
  ;; (let [area (g/get-current-area game)]
  ;;   (str (:name area) " [" (:type area) "]\n\n"
  ;;        (get-tree-info game)))
)

