(ns zork-fortress.cmds.history
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
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
  (str "\n> " (:trigger (:command turn)) "\n\n" (:response turn) "\n"))

(t/ann get-turn-history [t2/Game t/AnyInteger -> String])
(defn get-turn-history
  "Return a string of the given number of recent turns."
  [game num-to-show]
  (str (w/walk (fn [a] {:pre [((t/pred t2/Turn) a)]} (turn-history-string a))   
               (fn [a] {:pre [((t/pred (t/U nil (t/Coll t/Any))) a)]} (apply str a))
               (if (< (count (:turn-history game)) num-to-show)
                 (:turn-history game)
                 (subvec (:turn-history game) (- (count (:turn-history game)) num-to-show))))))

(t/ann get-last-turn-for-history [t2/Game -> (t/Option t2/Turn)])
(defn get-last-turn-for-history
  "Return last turn to show in history or nil if none."
  [game]
  (when (and (not (nil? (:last-turn game)))
             (not= true (:invalid (:last-turn game)))
             (not= 'history (:trigger (:command (:last-turn game)))))
    (:last-turn game)))

(t/ann history-cmd [t2/Game & :optional {:args (t/Vec String)} -> String])
(defn history-cmd
  "The history command."
  [game & {:keys [args] :or {args [(str default-history-show-count)]}}]
  (let [valid-last-turn (get-last-turn-for-history game)
        total-to-show (Integer/parseInt (or (get args 0) (str default-history-show-count)))
        num-to-show-from-history (if (nil? valid-last-turn)
                                    total-to-show
                                    (- total-to-show 1))
        show-last-turn (and (not (nil? valid-last-turn)) (not= total-to-show num-to-show-from-history))]
    (str "*** START HISTORY ***"
         (get-turn-history game num-to-show-from-history)
         (if show-last-turn (turn-history-string valid-last-turn) "")
         "*** END HISTORY ***")))
