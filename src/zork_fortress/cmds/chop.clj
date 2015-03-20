(ns zork-fortress.cmds.chop
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]
            [zork-fortress.area :as a]
            [zork-fortress.inventory :as inv]))

(t/ann no-trees-msg String)
(def no-trees-msg
  "There are no trees to chop.")

(t/ann invalid-trees-msg String)
(def invalid-trees-msg
  "That tree doesn't exist.")

(t/ann logs-received-msg [t/Int String -> String])
(defn logs-received-msg
  "Create a string showing the count and type of logs chopped."
  [log-count log-type]
  (str "Received " log-count " " log-type
       (if (= 1 log-count) " log." " logs.")))

(t/ann chop-cmd [t2/Game (t/Vec String) -> String])
(defn chop-cmd
  "The chop command."
  [game args]
  (let [tree-type (first args)
        area (g/get-current-area game)]
    (if (nil? tree-type)
      invalid-trees-msg
      (if (nil? (:trees area))
        no-trees-msg
        (let [tree (a/get-next-tree-of-type game area tree-type)]
          (if tree
            (let [log-count (:log-count tree)]
              (if (or (nil? log-count) (= 0 log-count))
                invalid-trees-msg
                (logs-received-msg log-count tree-type)))
            invalid-trees-msg))))))

(t/ann chop-cmd-effects [t2/Game (t/Vec String) -> t2/Game])
(defn chop-cmd-effects
  "The chop command changes to the game."
  [game args]
  (if (empty? args)
    game
    (or
     (let [tree-type (first args)
           area (g/get-current-area game)]
       (when (some? tree-type)
         (when (some? (:trees area))
           (let [tree (a/get-next-tree-of-type game area tree-type)]
             (when tree
               (inv/add-logs-to-inventory (a/remove-tree-from-area game area tree) tree))))))
     game)))
