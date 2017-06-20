(ns zork-fortress.core
  (:require [zork-fortress.ui :as ui]
            [zork-fortress.game :as g])
  (:gen-class))

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

(defn -main
  "Main entry point to start the game and get into the game loop."
  [& args]
  (println (ui/welcome-message))
  (game-loop (g/get-new-game)))
