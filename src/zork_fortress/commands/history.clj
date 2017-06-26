(ns zork-fortress.commands.history
  (:require [clojure.string :as str]
            [clojure.walk :as w]))

(def default-history-show-count 4)

(def history-command-help
  "The history command shows your last few moves.")

(defn turn-history-string
  "Return the history output for the turn."
  [turn]
  (str "\n> " (-> turn :command :trigger)
       (when (seq (-> turn :command :args))
         (str " " (str/join " " (-> turn :command :args))))
       "\n" (:response turn) "\n"))

(defn get-turn-history-string
  "Return a string of the given number of recent turns."
  [history display-count]
  (str (w/walk (fn [a] (turn-history-string a))
               (fn [a] (apply str a))
               (subvec history (int (max 0 (- (count history) display-count)))))))

(defn show-turn-in-history
  "Return last turn to show in history or nil if none."
  [turn]
  (when (and (some? turn)
             (not (-> turn :invalid))
             (not= 'history (-> turn :command :trigger))
             (not= 'help (-> turn :command :trigger)))
    turn))

(defn history-command
  "The history command."
  [game args]
  (let [valid-last-turn (show-turn-in-history (:last-turn game))
        total-to-show (Integer/parseInt (or (get args 0) (str default-history-show-count)))
        history-display-count (if (nil? valid-last-turn)
                                total-to-show
                                (- total-to-show 1))
        show-last-turn (and valid-last-turn (not= total-to-show history-display-count))]
    (str "*** START HISTORY ***"
         (get-turn-history-string (:turn-history game) history-display-count)
         (when show-last-turn (turn-history-string valid-last-turn))
         "*** END HISTORY ***")))
