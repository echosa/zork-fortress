(ns zork-fortress.cmds.look
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  (let [area-id (:current-area game)
        areas (:areas (:world game))
        area (first (filterv (fn [item] (= (:id item) area-id)) areas))]
    (str (:name area) " [" (:type area) "]\n")))

