(ns zork-fortress.cmds.history
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [clojure.string :as str]
            [clojure.walk :as w]))

(t/ann default-history-show-count t/Int)
(def default-history-show-count 4)

(t/ann history-cmd-help String)
(def history-cmd-help 
  "The history command shows your last few moves.")

(t/ann turn-history-string [t2/Turn -> String])
(defn turn-history-string
  "Return the history output for the turn."
  [turn]
  (str "\n> " (-> turn :command :trigger)
       (when (seq (-> turn :command :args))
         (str " " (str/join " " (-> turn :command :args))))
       "\n" (:response turn) "\n"))

(t/ann get-turn-history-string [t2/TurnHistory t/AnyInteger -> String])
(defn get-turn-history-string
  "Return a string of the given number of recent turns."
  [history display-count]
  (str (w/walk (fn [a] {:pre [((t/pred t2/Turn) a)]} (turn-history-string a))   
               (fn [a] {:pre [((t/pred (t/U nil (t/Coll t/Any))) a)]} (apply str a))
               (subvec history (int (max 0 (- (count history) display-count)))))))

(t/ann show-turn-in-history [(t/Option t2/Turn) -> (t/Option t2/Turn)])
(defn show-turn-in-history
  "Return last turn to show in history or nil if none."
  [turn]
  (when (and (some? turn)
             (not (-> turn :invalid))
             (not= 'history (-> turn :command :trigger))
             (not= 'help (-> turn :command :trigger)))
    turn))

(t/ann history-cmd [t2/Game (t/Vec String) -> String])
(defn history-cmd
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
