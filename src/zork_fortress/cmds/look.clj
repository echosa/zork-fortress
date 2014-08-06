(ns zork-fortress.cmds.look
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  (let [area (:current-area game)]
    (str (:name area) " [" (:type area) "]\n")))

