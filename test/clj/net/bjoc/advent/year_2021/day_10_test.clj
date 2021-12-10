(ns net.bjoc.advent.year-2021.day-10-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-10]))

(deftest check-by-examples
  (is (= :good (:status (check "([])"))))
  (is (= :good (:status (check "{()()()}"))))
  (is (= :good (:status (check "<([{}])>"))))
  (is (= :good (:status (check "[<>({}){}[([])<>]]"))))
  (is (= :good (:status (check "(((((((((())))))))))"))))
  (is (= :incomplete (:status (check "([]"))))
  (is (= :incomplete (:status (check "{()()()"))))
  (is (= :incomplete (:status (check "<([{}]"))))
  (is (= :incomplete (:status (check "[<>({}){}[([]"))))
  (is (= :incomplete (:status (check "(((((((((()))))"))))
  (is (= :corrupt (:status (check "(]"))))
  (is (= :corrupt (:status (check "{()()()>"))))
  (is (= :corrupt (:status (check "(((()))}"))))
  (is (= :corrupt (:status (check "<([]){()}[{}])")))))

(deftest total-score-by-example
  (is (= 26397 (file->total-syntax-score "data/year_2021/day_10_example1.input"))))

(deftest auto-score-by-examples
  (is (= 288957 (auto-score "[({(<(())[]>[[{[]{<()<>>")))
  (is (= 5566 (auto-score "[(()[<>])]({[<{<<[]>>(")))
  (is (= 1480781 (auto-score "(((({<>}<{<{<>}{[]{[]{}")))
  (is (= 995444 (auto-score "{<[[]]>}<{[{[{[]{()[[[]")))
  (is (= 294 (auto-score "<{([{{}}[<[[[<>{}]]]>[]]"))))

(deftest middle-auto-score-by-example
  (is (= 288957 (file->middle-auto-score "data/year_2021/day_10_example1.input"))))
