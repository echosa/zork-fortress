(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [clojure.walk :as w]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  "You see nothing.")

(t/ann clojure.walk/walk [[t/Any -> t/Any] [t/Any -> t/Any] t/Any -> t/Any])
(t/ann history-cmd [t2/Game -> String])
(defn history-cmd
  "The history command."
  [game]
  (str "*** START HISTORY ***"
       (w/walk #(str "\n> " (:command %) "\n\n" (:response %) "\n")
               (fn [a] {:pre [((t/pred (t/U nil (t/Coll t/Any))) a)]} (apply str a))
               (if (< (count (:turn-history game)) 4)
                 (:turn-history game)
                 (subvec (:turn-history game) (- (count (:turn-history game)) 4))))
       "*** END HISTORY ***"))

(t/ann run-cmd [t2/Game t/Symbol -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (merge game 
         {:last-turn (merge {:command command}
                             (condp = command
                               'look {:response (look-cmd game)}
                               'history {:response (history-cmd game)}
                               {:response "Invalid command." :invalid true}))
          :turn-history (if (and (:last-turn game)
                                 (not (:invalid (:last-turn game)))
                                 (not (= 'history (:command (:last-turn game)))))
                          (conj (:turn-history game) (:last-turn game))
                          (:turn-history game))}))

