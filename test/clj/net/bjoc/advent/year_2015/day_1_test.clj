(ns net.bjoc.advent.year-2015.day-1-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2015.day-1]))

(deftest resolve-directions-by-examples
  (is (= 0 (resolve-directions "(())")))
  (is (= 0 (resolve-directions "()()")))
  (is (= 3 (resolve-directions "(((")))
  (is (= 3 (resolve-directions "(()(()(")))
  (is (= 3 (resolve-directions "))(((((")))
  (is (= -1 (resolve-directions "())")))
  (is (= -1 (resolve-directions "))(")))
  (is (= -3 (resolve-directions ")))")))
  (is (= -3 (resolve-directions ")())())"))))

(deftest basement-entry-by-examples
  (is (= 1 (basement-entry ")")))
  (is (= 5 (basement-entry "()())"))))
