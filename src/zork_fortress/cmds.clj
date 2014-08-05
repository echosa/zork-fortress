(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2])
  (:use [zork-fortress.cmds.history :only [history-cmd get-last-turn-for-history]]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  "You see nothing.")

(t/ann run-cmd [t2/Game t2/Command -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (let [trigger (:trigger command)
        args (when (:args command) (:args command))
        last-turn-for-history (get-last-turn-for-history game)]
    (merge game 
           {:last-turn (merge {:command command}
                              (condp = trigger
                                'look {:response (look-cmd game)}
                                'history {:response (if (nil? args)
                                                      (history-cmd game)
                                                      (history-cmd game :args args))}
                                {:response "Invalid command." :invalid true}))
            :turn-history (if last-turn-for-history
                            (conj (:turn-history game) last-turn-for-history)
                            (:turn-history game))})))


