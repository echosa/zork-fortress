(ns zork-fortress.cmds.chop
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]))

(t/ann no-trees-msg String)
(def no-trees-msg
  "There are no trees to chop.")

(t/ann logs-received-msg [t/Int String -> String])
(defn logs-received-msg
  "Create a string showing the count and type of logs chopped."
  [log-count log-type]
  (str "Received " log-count " " log-type
       (if (= 1 log-count) " log." " logs.")))

(t/ann chop-cmd [t2/Game -> String])
(defn chop-cmd
  "The chop command."
  [game]
  (let [area (g/get-current-area game)]
    (if (nil? (:trees area))
      no-trees-msg
      "Received 10 oak logs.")))

