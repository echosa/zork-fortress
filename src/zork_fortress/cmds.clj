(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2])
  (:use [zork-fortress.cmds.history :only [history-cmd]]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  "You see nothing.")

(t/ann run-cmd [t2/Game t2/Command -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game cmd]
  (let [command (:command cmd)
        args (when (:args cmd) (:args cmd))]
    (merge game 
           {:last-turn (merge {:command command}
                              (condp = command
                                'look {:response (look-cmd game)}
                                'history {:response (if (nil? args)
                                                      (history-cmd game)
                                                      (history-cmd game :args args))}
                                {:response "Invalid command." :invalid true}))
            :turn-history (if (and (:last-turn game)
                                   (not (:invalid (:last-turn game)))
                                   (not (= 'history (:command (:last-turn game)))))
                            (conj (:turn-history game) (:last-turn game))
                            (:turn-history game))})))


