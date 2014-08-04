(ns zork-fortress.cmds-test
  (:require [clojure.test :refer :all]
            [zork-fortress.cmds :refer :all]
            [zork-fortress.test-helpers :as h]))

(deftest test-commands-should-set-last-turn
  (testing "Look command should return message."
    (let [game (h/get-test-game)]
      (is (= nil (:last-turn game))
          (= {:command 'look :response "You see nothing."}
             (:last-turn (run-cmd game 'look)))))))

(deftest test-commands-should-set-history
  (testing "Look command should return message."
    (let [game (h/get-test-game :last-turn {:command 'prev
                                            :response "Previous command."})]
      (is (= [] (:turn-history game))
          (= [{:command 'prev :response "Previous command."}]
             (:turn-history (run-cmd game 'look)))))))

(deftest test-invalid-commands-are-marked-as-such
  (testing "Invalid command should respond as such."
    (let [game (h/get-test-game)]
      (is (= {:command 'foobar
              :response "Invalid command."
              :invalid true}
              (:last-turn (run-cmd game 'foobar)))))))

(deftest test-valid-commands-should-be-added-to-history
  (testing "Valid commands should be added to the command history."
    (let [last-turn {:command 'previous
                     :response "Previous message."}
          turn-history [{:command 'first
                         :response "First message."}]
          game (h/get-test-game :last-turn last-turn
                                :turn-history turn-history)]
      (is (= (conj turn-history last-turn)
             (:turn-history (run-cmd game 'look)))))))

(deftest test-invalid-commands-should-not-be-added-to-history
  (testing "Valid commands should be added to the command history."
    (let [last-turn {:command 'foobar
                     :response "Invalid command."
                     :invalid true}
          turn-history [{:command 'first
                         :response "First message."}]
          game (h/get-test-game :last-turn last-turn
                                :turn-history turn-history)]
      (is (= turn-history
             (:turn-history (run-cmd game 'look)))))))


(deftest test-history-command-should-default-to-4-items
  (testing "History command should display 4 items."
    (let [game (h/get-test-game :last-turn {:command 'foobar
                                            :response "Invalid command."
                                            :invalid true}
                                :turn-history [{:command 'first
                                                :response "First message."}
                                               {:command 'second
                                                :response "Second message."}
                                               {:command 'third
                                                :response "Third message."}
                                               {:command 'fourth
                                                :response "Fourth message."}
                                               {:command 'fifth
                                                :response "Fifth message."}])]
      (is (= "*** START HISTORY ***
> second

Second message.

> third

Third message.

> fourth

Fourth message.

> fifth

Fifth message.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game 'history))))))))


(deftest test-history-command-with-less-than-4-should-display-less-than-4-items
  (testing "History command should be able to display less than 4 items."
    (let [game (h/get-test-game :last-turn {:command 'foobar
                                            :response "Invalid command."
                                            :invalid true}
                                :turn-history [{:command 'first
                                                :response "First message."}
                                               {:command 'second
                                                :response "Second message."}
                                               {:command 'third
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> first

First message.

> second

Second message.

> third

Third message.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game 'history))))))))

(deftest test-history-command-should-not-be-added-to-history
  (testing "History command should not be added to the history."
    (let [game (h/get-test-game :last-turn {:command 'history
                                            :response "*** START HISTORY ***
> first

First message.

> second

Second message.

> third

Third message.
*** END HISTORY ***"}
                                :turn-history [{:command 'first
                                                :response "First message."}
                                               {:command 'second
                                                :response "Second message."}
                                               {:command 'third
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> first

First message.

> second

Second message.

> third

Third message.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game 'history))))))))

(deftest test-history-command-should-display-last-turn-if-valid
  (testing "History command should display the last turn, if it was valid."
    (let [game (h/get-test-game :last-turn {:command 'look
                                            :response "You see nothing."}
                                :turn-history [{:command 'first
                                                :response "First message."}
                                               {:command 'second
                                                :response "Second message."}
                                               {:command 'third
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> first

First message.

> second

Second message.

> third

Third message.

> look

You see nothing.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game 'history))))))))

(deftest test-history-command-should-limit-display-to-given-count
  (testing "History command should limit its display to the given count."
    (let [game (h/get-test-game :last-turn {:command 'look
                                            :response "You see nothing."}
                                :turn-history [{:command 'first
                                                :response "First message."}
                                               {:command 'second
                                                :response "Second message."}
                                               {:command 'third
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> third

Third message.

> look

You see nothing.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game 'history :args [2]))))))))
