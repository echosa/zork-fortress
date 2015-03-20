(ns zork-fortress.core
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.ui :as ui]
            [zork-fortress.game :as g])
  (:gen-class))

(t/ann game-loop [t2/Game & :optional {:show-last-response t/Bool} -> nil])
(defn- game-loop
  "The main game loop."
  [game & {:keys [show-last-response] :or [show-last-response false]}]
  (when show-last-response
    (ui/print-last-response game))
  (ui/print-prompt game)
  (flush)
  (let [updated-game (ui/process-user-input game)]
    (when updated-game
      (game-loop updated-game
                 :show-last-response (or (not= game updated-game)
                                         (= 'history (-> game :last-turn :command :trigger)))))))

(t/ann -main [& :optional {:args t/Any} -> t/Any])
(defn -main
  "Main entry point to start the game and get into the game loop."
  [& args]
  (println (ui/welcome-message))
  (game-loop (g/get-new-game)))
