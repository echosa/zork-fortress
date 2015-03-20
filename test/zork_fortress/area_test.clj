(ns zork-fortress.area-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.area :as a]
            [zork-fortress.game :as g]))

(deftest test-getting-area-from-game
  (testing "Getting area from game by ID should return correct area"
    (let [game (h/get-test-game)]
      (is (= (h/get-test-area)
             (a/get-area-from-game game 1))))))

(deftest test-adding-tree
  (testing "Correct tree should be added to the correct area."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "oak" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}
                                   {:id 2 :type "oak" :log-count 5}]}]
             (:trees (a/get-area-from-game game 1)))))))

(deftest test-adding-different-tree-type
  (testing "Correct tree should be added to the correct area."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "pine" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}
              {:type "pine" :trees [{:id 1 :type "pine" :log-count 5}]}]
             (:trees (a/get-area-from-game game 1)))))))

(deftest test-adding-multiple-tree-types
  (testing "Look command should print tree information as part of its response."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "oak" 5)
          game (a/add-tree-to-area game 1 "pine" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}
                                   {:id 2 :type "oak" :log-count 5}]}
              {:type "pine" :trees [{:id 1 :type "pine" :log-count 5}]}]
             (:trees (a/get-area-from-game game 1)))))))

(deftest test-getting-next-tree-of-type
  (testing "Should return the next tree available for the type."
    (let [game (h/get-test-game)
          area (a/get-area-from-game game 1)
          tree-type "oak"]
      (is (= {:id 1 :type tree-type :log-count 10}
             (a/get-next-tree-of-type game area tree-type))))))

(deftest test-getting-tree-type
  (testing "Correct tree type listing should be returned."
    (let [game (h/get-test-game)]
      (is (= {:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}
             (a/get-tree-type-from-area game 1 "oak"))))))

(deftest test-getting-next-tree-id
  (testing "Correct id should be returned."
    (let [game (h/get-test-game)]
      (is (= 2 (a/get-next-tree-id-to-add game 1 "oak"))))))

(deftest test-getting-next-tree-id-for-nonexisting-type
  (testing "Correct id should be returned."
    (let [game (h/get-test-game)]
      (is (= 1 (a/get-next-tree-id-to-add game 1 "pine"))))))

(deftest test-getting-tree-types-without-type
  (testing "Should get tree type vector without given type."
    (let [game (h/get-test-game)]
      (is (= [] (a/get-tree-types-from-area-without-type game 1 "oak"))))))

(deftest test-area-contains-tree-with-id
  (testing "Should return id of the existing tree."
    (let [area (h/get-test-area)]
      (is (= {:id 1 :type "oak" :log-count 10}
             (a/area-has-tree-with-id area "oak" 1))))))

(deftest test-area-does-not-contain-tree-with-id
  (testing "Should return nil if tree-id doesn't exist."
    (let [area (h/get-test-area)]
      (is (= nil (a/area-has-tree-with-id area "oak" 0))))))

(deftest test-removing-tree
  (testing "Correct tree should be removed from the correct area."
    (let [game (h/get-test-game)
          game (a/remove-tree-from-area game (g/get-current-area game) {:id 1 :type "oak"})]
      (is (= nil (a/area-has-tree-with-id (g/get-current-area game) "oak" 1)))
      (is (= [] (a/get-trees-with-type-from-area (g/get-current-area game) "oak"))))))
