(ns zork-fortress.game
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann get-current-area [t2/Game -> t2/Area])
(defn get-current-area
  "Returns the current area the player is in."
  [game]
  {:pre [(not (empty? (:areas (:world game))))]}
  (nth (filterv (fn [item] (= (:id item) (:current-area game)))
                (:areas (:world game)))
       0))
