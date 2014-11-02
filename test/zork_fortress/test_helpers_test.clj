(ns zork-fortress.test-helpers-test
  (:require [clojure.test :refer :all])
  (:use [zork-fortress.test-helpers :as h]))

(deftest test-getting-default-test-player
  (testing "Getting a player without specifiying a name should default to Player."
    (let [player (h/get-test-player)]
      (is (= "Player" (:name player))))))

(deftest test-getting-test-player-with-name
  (testing "Getting a player with a name should have that name."
    (let [name "Herp"
          player (h/get-test-player :name name)]
      (is (= name (:name player))))))

(deftest test-default-test-area
  (testing "Getting an area should have expected default data."
    (let [area (h/get-test-area)]
      (is (= 1 (:id area)))
      (is (= "First Area" (:name area)))
      (is (= "plains" (:type area)))
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}]
             (:trees area))))))

(deftest test-area-without-trees
  (testing "Getting an area without trees should have no trees."
    (let [area (h/get-test-area :without-trees true)]
      (is (= nil (:trees area))))))

(deftest test-default-test-world
  (testing "Getting a world should have expected default data."
    (let [area (h/get-test-area)
          world (h/get-test-world)]
      (is (= area (first (:areas world)))))))

(deftest test-world-with-no-trees
  (testing "Getting a world without trees should have no trees."
    (let [world (h/get-test-world :without-trees true)]
      (is (= nil (:trees (first (:areas world))))))))

(deftest test-default-test-game
  (testing "Getting a game should have expected default data."
    (let [player (h/get-test-player)
          world (h/get-test-world)
          game (h/get-test-game)]
      (is (= player (:player game)))
      (is (= world (:world game)))
      (is (= 1 (:current-area game)))
      (is (= [] (:turn-history game))))))

(deftest test-game-with-no-trees
  (testing "Getting a game without trees should have no trees."
    (let [game (h/get-test-game :without-trees true)]
      (is (= nil (:trees (first (:areas (:world game)))))))))

(deftest test-game-with-last-turn
  (testing "Getting a game should have the given last turn."
    (let [player (h/get-test-player)
          world (h/get-test-world)
          last-turn {:command 'cmd :response "Response."}
          game (h/get-test-game :last-turn last-turn)]
      (is (= player (:player game)))
      (is (= world (:world game)))
      (is (= 1 (:current-area game)))
      (is (= [] (:turn-history game)))
      (is (= last-turn (:last-turn game))))))

(deftest test-game-with-turn-history
  (testing "Getting a game should have the given turn history."
    (let [player (h/get-test-player)
          world (h/get-test-world)
          turn-history [{:command 'cmd :response "Response."}]
          game (h/get-test-game :turn-history turn-history)]
      (is (= player (:player game)))
      (is (= world (:world game)))
      (is (= 1 (:current-area game)))
      (is (= turn-history (:turn-history game))))))

(deftest test-game-with-last-turn-and-turn-history
  (testing "Getting a game should have the given last turn and turn history."
    (let [player (h/get-test-player)
          world (h/get-test-world)
          last-turn {:command 'cmd :response "Response."}
          turn-history [{:command 'cmd2 :response "Response 2."}]
          game (h/get-test-game :last-turn last-turn :turn-history turn-history)]
      (is (= player (:player game)))
      (is (= world (:world game)))
      (is (= 1 (:current-area game)))
      (is (= last-turn (:last-turn game)))
      (is (= turn-history (:turn-history game))))))
