(ns zork-fortress.cmds.look-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g])
  (:use [zork-fortress.cmds :only [run-cmd]]))

(deftest test-looking-should-print-area-name
  (testing "Look command should print the area name as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'look})))
                     "First Area")))))

(deftest test-looking-should-print-area-type
  (testing "Look command should print the area type as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'look})))
                     "plains")))))

(deftest test-looking-should-print-tree-info
  (testing "Look command should print tree information as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'look})))
                     "You see 1 oak tree.")))))

(deftest test-looking-should-print-multiple-tree-info
  (testing "Look command should print tree information as part of its response."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "oak" 5)
          game (g/add-tree-to-area game 1 "pine" 5)
          response (:response (:last-turn (run-cmd game {:trigger 'look})))]
      (is (.contains response "You see 2 oak trees."))
      (is (.contains response "You see 1 pine tree.")))))
