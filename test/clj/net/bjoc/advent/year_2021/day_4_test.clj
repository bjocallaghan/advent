(ns net.bjoc.advent.year-2021.day-4-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-4]))

(def test-card (first (file->cards "data/year_2021/day_4_example1.input")))

(deftest mark-and-score
  (testing "Serial marking"
    (is (= 266 (-> test-card
                  (mark 5)
                  (mark 15)
                  (mark 1000)
                  (mark 14)
                  score))))
  (testing "Variadic marking"
    (is (= 266 (-> test-card (mark 5 15 1000 14) score)))))

(deftest bingo?-by-examples
  (let [almost-bingo (mark test-card 8 2 23 4 13 9 10)]
    (is (not (bingo? almost-bingo)))
    (is (bingo? (mark almost-bingo 12)))
    (is (bingo? (mark almost-bingo 24)))))

(deftest game-score-by-example
  (is (= 4512 (file->game-score "data/year_2021/day_4_example1.input"))))

(deftest bad-game-score-by-example
  (is (= 1924 (file->bad-game-score "data/year_2021/day_4_example1.input"))))
