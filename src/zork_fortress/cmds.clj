(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann run-cmd [t2/Game t/Symbol -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (merge game
         {:last-turn {:command 'look
                      :response "You see nothing."}}))