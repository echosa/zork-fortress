(ns zork-fortress.game-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]))

(deftest test-getting-current-area-from-game
  (testing "Getting current area from game should return correct area"
    (let [game (h/get-test-game)]
      (is (= (h/get-test-area)
             (g/get-current-area game))))))

(deftest test-getting-area-from-game
  (testing "Getting area from game by ID should return correct area"
    (let [game (h/get-test-game)]
      (is (= (h/get-test-area)
             (g/get-area-from-game game 1))))))

(deftest test-getting-current-area-tree-counts
  (testing "Should get correct tree counts from game."
    (let [game (h/get-test-game)]
      (is (= {"oak" 1}
             (g/get-current-area-tree-counts game))))))

(deftest test-adding-tree
  (testing "Correct tree should be added to the correct area."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "oak" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}
                                   {:id 2 :type "oak" :log-count 5}]}]
             (:trees (g/get-area-from-game game 1)))))))

(deftest test-getting-tree-type
  (testing "Correct tree type listing should be returned."
    (let [game (h/get-test-game)]
      (is (= {:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}
             (g/get-tree-type-from-area game 1 "oak"))))))

(deftest test-getting-next-tree-id
  (testing "Correct id should be returned."
    (let [game (h/get-test-game)]
      (is (= 2 (g/get-next-tree-id-to-add game 1 "oak"))))))

(deftest test-getting-next-tree-id-for-nonexisting-type
  (testing "Correct id should be returned."
    (let [game (h/get-test-game)]
      (is (= 1 (g/get-next-tree-id-to-add game 1 "pine"))))))

(deftest test-getting-tree-types-without-type
  (testing "Should get tree type vector without given type."
    (let [game (h/get-test-game)]
      (is (= []
             (g/get-tree-types-from-area-without-type game 1 "oak"))))))

(deftest test-adding-different-tree-type
  (testing "Correct tree should be added to the correct area."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "pine" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}
              {:type "pine" :trees [{:id 1 :type "pine" :log-count 5}]}]
             (:trees (g/get-area-from-game game 1)))))))

(deftest test-adding-multiple-tree-types
  (testing "Look command should print tree information as part of its response."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "oak" 5)
          game (g/add-tree-to-area game 1 "pine" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}
                                   {:id 2 :type "oak" :log-count 5}]}
              {:type "pine" :trees [{:id 1 :type "pine" :log-count 5}]}]
             (:trees (g/get-area-from-game game 1)))))))

(deftest test-getting-current-area-mutliple-tree-counts
  (testing "Should get correct tree counts from game."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "oak" 5)
          game (g/add-tree-to-area game 1 "pine" 5)
          game (g/add-tree-to-area game 1 "cedar" 7)
          game (g/add-tree-to-area game 1 "pine" 2)]
      (is (= {"oak" 2 "pine" 2 "cedar" 1}
             (g/get-current-area-tree-counts game))))))

(deftest test-area-contains-tree-with-id
  (testing "Should return id of the existing tree."
    (let [area (h/get-test-area)]
      (is (= {:id 1 :type "oak" :log-count 10}
             (g/area-has-tree-with-id area "oak" 1))))))

(deftest test-area-does-not-contain-tree-with-id
  (testing "Should return nil if tree-id doesn't exist."
    (let [area (h/get-test-area)]
      (is (= nil (g/area-has-tree-with-id area "oak" 0))))))

(deftest test-getting-next-tree-of-type
  (testing "Should return the next tree available for the type."
    (let [game (h/get-test-game)
          area (g/get-area-from-game game 1)
          tree-type "oak"]
      (is (= {:id 1 :type tree-type :log-count 10}
             (g/get-next-tree-of-type game area tree-type))))))

(deftest test-removing-tree
  (testing "Correct tree should be removed from the correct area."
    (let [game (h/get-test-game)
          game (g/remove-tree-from-area game (g/get-current-area game) {:id 1 :type "oak"})]
      (is (= nil (g/area-has-tree-with-id (g/get-current-area game) "oak" 1)))
      (is (= [] (g/get-trees-with-type-from-area (g/get-current-area game) "oak"))))))

(deftest test-adding-multiple-logs-to-inventory
  (testing "Player should have both types of logs in inventory."
    (let [game (h/get-test-game)
          game (g/add-logs-to-inventory game {:id 1 :type "pine" :log-count 5})
          game (g/add-logs-to-inventory game {:id 1 :type "oak" :log-count 2})]
      (is (= 5 (g/get-inventory-log-count game "pine")))
      (is (= 2 (g/get-inventory-log-count game "oak"))))))

(deftest test-adding-multiple-of-the-same-log-type-to-inventory
  (testing "Player should have the correct number of logs in inventory."
    (let [game (h/get-test-game)
          game (g/add-logs-to-inventory game {:id 1 :type "oak" :log-count 5})
          game (g/add-logs-to-inventory game {:id 2 :type "oak" :log-count 2})]
      (is (= 7 (g/get-inventory-log-count game "oak"))))))
