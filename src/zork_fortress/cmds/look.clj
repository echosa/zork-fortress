(ns zork-fortress.cmds.look
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  (let [area (g/get-current-area game)]
    (str (:name area) " [" (:type area) "]\n\n"
         "You see 1 oak tree.\n")))

