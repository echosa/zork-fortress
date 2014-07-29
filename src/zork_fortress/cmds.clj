(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  "You see nothing.")

(t/ann run-cmd [t2/Game t/Symbol -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (merge game 
         {:last-turn (merge {:command command}
                             (condp = command
                               'look {:response (look-cmd game)}
                               {:response "Invalid command." :invalid true}))
          :turn-history (if (and (:last-turn game) (not (:invalid (:last-turn game))))
                          (conj (:turn-history game) (:last-turn game))
                          (:turn-history game))}))

