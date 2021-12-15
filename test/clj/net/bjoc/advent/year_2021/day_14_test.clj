(ns net.bjoc.advent.year-2021.day-14-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2021.day-14]))

(deftest grow-by-examples
  (let [filename "data/year_2021/day_14_example1.input"
        grow (grow-fn (file->rules filename))
        chain (file->chain filename)]
    (is (= "NCNBCHB" (grow chain 1)))
    (is (= "NBCCNBBBCBHCB" (grow chain 2)))
    (is (= "NBBBCNCCNBBNBNBBCHBHHBCHB" (grow chain 3)))
    (is (= "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB" (grow chain 4)))))

(deftest freqs-after-growth-by-examples
  (let [filename "data/year_2021/day_14_example1.input"
        chain (file->chain filename)
        rules (file->rules filename)]
    (is (= 1749 ((freqs-after-growth chain rules 10) \B)))
    (is (= 298 ((freqs-after-growth chain rules 10) \C)))
    (is (= 161 ((freqs-after-growth chain rules 10) \H)))
    (is (= 865 ((freqs-after-growth chain rules 10) \N)))))

(deftest freqs-after-growth*-by-examples
  (let [filename "data/year_2021/day_14_example1.input"
        chain (file->chain filename)
        rules (file->rules filename)]
    (is (= 1749 ((freqs-after-growth* chain rules 10) \B)))
    (is (= 298 ((freqs-after-growth* chain rules 10) \C)))
    (is (= 161 ((freqs-after-growth* chain rules 10) \H)))
    (is (= 865 ((freqs-after-growth* chain rules 10) \N)))))

(deftest freqs-after-growth**-by-examples
  (let [filename "data/year_2021/day_14_example1.input"
        chain (file->chain filename)
        rules (file->rules filename)]
    (is (= 1749 ((freqs-after-growth** chain rules 10) \B)))
    (is (= 298 ((freqs-after-growth** chain rules 10) \C)))
    (is (= 161 ((freqs-after-growth** chain rules 10) \H)))
    (is (= 865 ((freqs-after-growth** chain rules 10) \N)))
    (is (= 2192039569602 ((freqs-after-growth** chain rules 40) \B)))
    (is (= 3849876073 ((freqs-after-growth** chain rules 40) \H)))))

(deftest freqs-after-growth***-by-examples
  (let [filename "data/year_2021/day_14_example1.input"
        chain (file->chain filename)
        rules (file->rules filename)]
    (is (= 1749 ((freqs-after-growth*** chain rules 10) \B)))
    (is (= 298 ((freqs-after-growth*** chain rules 10) \C)))
    (is (= 161 ((freqs-after-growth*** chain rules 10) \H)))
    (is (= 865 ((freqs-after-growth*** chain rules 10) \N)))
    (is (= 2192039569602 ((freqs-after-growth*** chain rules 40) \B)))
    (is (= 3849876073 ((freqs-after-growth*** chain rules 40) \H)))))

(deftest big-small-diff-by-example
  (is (= 1588 (file->big-small-diff "data/year_2021/day_14_example1.input")))
  (is (= 2188189693529 (file->big-small-diff "data/year_2021/day_14_example1.input"
                                             freqs-after-growth** 40)))
  (is (= 2188189693529 (file->big-small-diff "data/year_2021/day_14_example1.input"
                                             freqs-after-growth*** 40))))
