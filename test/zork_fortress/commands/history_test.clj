(ns zork-fortress.commands.history-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h])
  (:use [zork-fortress.commands :only [run-cmd]]))

(deftest test-valid-commands-should-be-added-to-history
  (testing "Valid commands should be added to the command history."
    (let [last-turn {:command {:trigger 'previous :args []}
                     :response "Previous message."}
          turn-history [{:command {:trigger 'first :args []}
                         :response "First message."}]
          game (h/get-test-game :last-turn last-turn
                                :turn-history turn-history)]
      (is (= (conj turn-history last-turn)
             (:turn-history (run-cmd game {:trigger 'look :args []})))))))

(deftest test-invalid-commands-should-not-be-added-to-history
  (testing "Valid commands should be added to the command history."
    (let [last-turn {:command {:trigger 'foobar :args []}
                     :response "Invalid command."
                     :invalid true}
          turn-history [{:command {:trigger 'first :args []}
                         :response "First message."}]
          game (h/get-test-game :last-turn last-turn
                                :turn-history turn-history)]
      (is (= turn-history
             (:turn-history (run-cmd game {:trigger 'look :args []})))))))


(deftest test-history-command-should-default-to-4-items
  (testing "History command should display 4 items."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'foobar :args []}
                                            :response "Invalid command."
                                            :invalid true}
                                :turn-history [{:command {:trigger 'first :args []}
                                                :response "First message."}
                                               {:command {:trigger 'second :args []}
                                                :response "Second message."}
                                               {:command {:trigger 'third :args []}
                                                :response "Third message."}
                                               {:command {:trigger 'fourth :args []}
                                                :response "Fourth message."}
                                               {:command {:trigger 'fifth :args []}
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
             (:response (:last-turn (run-cmd game {:trigger 'history :args []}))))))))


(deftest test-history-command-with-less-than-4-should-display-less-than-4-items
  (testing "History command should be able to display less than 4 items."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'foobar :args []}
                                            :response "Invalid command."
                                            :invalid true}
                                :turn-history [{:command {:trigger 'first :args []}
                                                :response "First message."}
                                               {:command {:trigger 'second :args []}
                                                :response "Second message."}
                                               {:command {:trigger 'third :args []}
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> first
First message.

> second
Second message.

> third
Third message.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game {:trigger 'history :args []}))))))))

(deftest test-history-command-should-not-be-added-to-history
  (testing "History command should not be added to the history."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'history :args []}
                                            :response "*** START HISTORY ***
> first
First message.

> second
Second message.

> third
Third message.
*** END HISTORY ***"}
                                :turn-history [{:command {:trigger 'first :args []}
                                                :response "First message."}
                                               {:command {:trigger 'second :args []}
                                                :response "Second message."}
                                               {:command {:trigger 'third :args []}
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> first
First message.

> second
Second message.

> third
Third message.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game {:trigger 'history :args []}))))))))

(deftest test-history-command-should-display-last-turn-if-valid
  (testing "History command should display the last turn, if it was valid."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'look :args []}
                                            :response "You see nothing."}
                                :turn-history [{:command {:trigger 'first :args []}
                                                :response "First message."}
                                               {:command {:trigger 'second :args []}
                                                :response "Second message."}
                                               {:command {:trigger 'third :args []}
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
             (:response (:last-turn (run-cmd game {:trigger 'history :args []}))))))))

(deftest test-history-command-should-limit-display-to-given-count
  (testing "History command should limit its display to the given count."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'look :args []}
                                            :response "You see nothing."}
                                :turn-history [{:command {:trigger 'first :args []}
                                                :response "First message."}
                                               {:command {:trigger 'second :args []}
                                                :response "Second message."}
                                               {:command {:trigger 'third :args []}
                                                :response "Third message."}])]
      (is (= "*** START HISTORY ***
> third
Third message.

> look
You see nothing.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd game {:trigger 'history :args ["2"]}))))))))

(deftest test-history-should-work-multiple-times
  (testing "Running the history command shouldn't clear the history."
    (let [game (h/get-test-game)]
      (is (= "*** START HISTORY ***
> look
First Area [plains]

You see 1 oak tree.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd
                                     (run-cmd
                                      (run-cmd game {:trigger 'look :args []})
                                      {:trigger 'history :args []})
                                     {:trigger 'history :args []}))))))))

(deftest test-history-should-show-command-arguments
  (testing "The arguments for commands should be shown in the history."
    (let [game (h/get-test-game)]
      (is (= "*** START HISTORY ***
> chop oak
Received 10 oak logs.
*** END HISTORY ***"
             (:response (:last-turn (run-cmd (run-cmd game {:trigger 'chop :args ["oak"]}) {:trigger 'history :args []}))))))))
