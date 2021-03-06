(ns zork-fortress.commands.chop-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.area :as a]
            [zork-fortress.inventory :as inv]
            [zork-fortress.commands.chop :as chop])
  (:use [zork-fortress.commands :only [run-command]]))

(deftest test-chopping-with-no-trees-should-display-an-error-msg
  (testing "Trying to chop trees where there are none should show an error message."
    (let [game (h/get-test-game :without-trees true)]
      (is (= chop/no-trees-msg
             (:response (:last-turn (run-command game "chop oak"))))))))

(deftest test-chopping-tree-should-display-log-count
  (testing "Chopping a tree should show the number of logs gotten from the tree."
    (let [game (h/get-test-game)]
      (is (= "Received 10 oak logs."
             (:response (:last-turn (run-command game "chop oak"))))))))

(deftest test-chopping-tree-with-single-log-should-display-singular-log-count
  (testing "Chopping a tree should show the number of logs gotten from the tree."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "pine" 1)]
      (is (= "Received 1 pine log."
             (:response (:last-turn (run-command game "chop pine"))))))))

(deftest test-chopping-invalid-tree-type
  (testing "Chopping a tree with invalid type should show an error message."
    (let [game (h/get-test-game)]
      (is (= "That tree doesn't exist."
             (:response (:last-turn (run-command game "chop foobar"))))))))

(deftest test-chopping-tree-should-add-logs-to-inventory
  (testing "Chopping a tree should add the logs to your inventory."
    (let [game (run-command (h/get-test-game) "chop oak")]
      (is (= 10 (inv/get-inventory-log-count game "oak"))))))

(deftest test-chopping-one-of-many-tree-types-should-add-logs-to-inventory
  (testing "Chopping one of many tree types should add the logs to your inventory."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "pine" 1)
          game (run-command game "chop pine")]
      (is (= 1 (inv/get-inventory-log-count game "pine"))))))

(deftest test-chopping-tree-should-remove-logs-from-area
  (testing "Chopping a tree should remove the logs from the area."
    (let [game (run-command (h/get-test-game) "chop oak")
          area (g/get-current-area game)]
      (is (empty? (:trees (a/get-tree-type-from-area game (:id area) "oak"))))
      (is (nil? (a/area-has-tree-with-id area "oak" 1))))))

(deftest test-chopping-tree-should-only-chop-one-tree
  (testing "Chopping a tree should only chop one tree."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "oak" 5)
          game (run-command game "chop oak")]
      (is (= [{:id 2 :type "oak" :log-count 5}]
             (a/get-trees-with-type-from-area (g/get-current-area game) "oak")))
      (is (nil? (a/area-has-tree-with-id (g/get-current-area game) "oak" 1))))))

(deftest test-chopping-unspecified-tree-with-only-one-type-available-should-chop-that-type
  (testing "Chopping an unspecified tree with only one type available."
    (let [game (h/get-test-game)
          game (run-command game "chop tree")]
      (is (= "Received 10 oak logs." (:response (:last-turn game))))
      (is (= 10 (inv/get-inventory-log-count game "oak"))))))

(deftest test-chopping-unspecified-tree-with-more-than-one-type-available-should-ask-for-clarification
  (testing "Chopping an unspecified tree with more than one type available should ask for clarification."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "pine" 1)]
      (is (= "Which tree would you like to chop?"
             (:response (:last-turn (run-command game "chop tree"))))))))
