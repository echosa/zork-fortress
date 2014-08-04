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

(t/ann history-cmd [t2/Game & :optional {:args (t/Vec t/AnyInteger)} -> String])
(defn history-cmd
  "The history command."
  [game & {:keys [args] :or {args [4]}}]
  (let [last-turn (when (and (not (nil? (:last-turn game)))
                             (not= true (:invalid (:last-turn game)))
                             (not= 'history (:command (:last-turn game))))
                    (:last-turn game))
        total-to-show (or (get args 0) 4)
        num-to-show-from-history (if (nil? last-turn)
                                    total-to-show
                                    (- total-to-show 1))
        show-last-turn (and (not (nil? last-turn)) (not= total-to-show num-to-show-from-history))]
    (str "*** START HISTORY ***"
         (w/walk (fn [a] {:pre [((t/pred t2/Turn) a)]} (turn-history-string a))
                 (fn [a] {:pre [((t/pred (t/U nil (t/Coll t/Any))) a)]} (apply str a))
                 (if (< (count (:turn-history game)) num-to-show-from-history)
                   (:turn-history game)
                   (subvec (:turn-history game) (- (count (:turn-history game)) num-to-show-from-history))))
         (if show-last-turn (turn-history-string last-turn) "")
         "*** END HISTORY ***")))

(t/ann run-cmd [t2/Game t/Symbol & :optional {:args (t/Vec t/AnyInteger)} -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command & {:keys [args]}]
  (merge game 
         {:last-turn (merge {:command command}
                             (condp = command
                               'look {:response (look-cmd game)}
                               'history {:response (if (nil? args) (history-cmd game) (history-cmd game :args args))}
                               {:response "Invalid command." :invalid true}))
          :turn-history (if (and (:last-turn game)
                                 (not (:invalid (:last-turn game)))
                                 (not (= 'history (:command (:last-turn game)))))
                          (conj (:turn-history game) (:last-turn game))
                          (:turn-history game))}))

