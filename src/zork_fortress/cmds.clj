(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [clojure.walk :as w]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  "You see nothing.")

(t/ann turn-history-string [t2/Turn -> String])
(defn turn-history-string
  "Return the history output for the turn."
  [turn]
  (str "\n> " (:command turn) "\n\n" (:response turn) "\n"))

(t/ann clojure.walk/walk [[t/Any -> t/Any] [t/Any -> t/Any] t/Any -> t/Any])
(t/ann history-cmd [t2/Game -> String])
(defn history-cmd
  "The history command."
  [game]
  (let [last-turn (when (and (not (nil? (:last-turn game)))
                             (not= true (:invalid (:last-turn game)))
                             (not= 'history (:command (:last-turn game))))
                    (:last-turn game))]
    (str "*** START HISTORY ***"
         (w/walk (fn [a] {:pre [((t/pred t2/Turn) a)]} (turn-history-string a))
                 (fn [a] {:pre [((t/pred (t/U nil (t/Coll t/Any))) a)]} (apply str a))
                 (if (< (count (:turn-history game)) 4)
                   (:turn-history game)
                   (subvec (:turn-history game) (- (count (:turn-history game)) 4))))
         (if (nil? last-turn) "" (turn-history-string last-turn))
         "*** END HISTORY ***")))

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

