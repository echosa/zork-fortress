(ns zork-fortress.commands.help
  (:require [clojure.walk :as w])
  (:import [clojure.lang Namespace]))

(def default-help
  "Which command you like help on? Try \"help <command>\" or \"help commands\" to see a list of commands.")

(def invalid-command-msg
  "No help for that topic.")

;; TODO Add/improving help docs for commands
(defn available-commands-list
  "Returns a list of command for which help is available."
  []
  (let [ns-prefix "zork-fortress.commands."
        command-ns (sort
                (comparator (fn [ns1 ns2] (compare (str ns1) (str ns2))))
                (filter (fn [ns]
                          (and (= -1 (.indexOf (str ns) "-test")) (not= -1 (.indexOf (str ns) ns-prefix)))) (all-ns)))]
    (str "Command list:\n\n"
         (w/walk (fn [ns] (str (subs (str ns) (count ns-prefix)) "\n"))
                 (fn [s] (apply str (sort s)))
                 command-ns))))

(defn help-command
  "Shows helpful information to the user."
  [args]
  (if (empty? args)
    default-help
    (let [command (first args)]
      (if (= command "commands")
        (available-commands-list)
        (let [command-ns (find-ns (symbol (str "zork-fortress.commands." command)))]
          (if (nil? command-ns)
            invalid-command-msg
            (let [help-msg (ns-resolve command-ns (symbol (str command "-command-help")))]
              (if (nil? help-msg)
                invalid-command-msg
                (if (var? help-msg)
                  (str (var-get help-msg))
                  invalid-command-msg)))))))))
