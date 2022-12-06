(ns net.bjoc.advent.year-2022.day-6-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2022.day-6 :refer :all]))

(deftest index-of-packet-by-examples
  (is (= 6 (index-of-packet "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
  (is (= 7 (index-of-packet* "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
  (is (= 4 (index-of-packet "bvwbjplbgvbhsrlpgdmjqwftvncz")))
  (is (= 5 (index-of-packet* "bvwbjplbgvbhsrlpgdmjqwftvncz")))
  (is (= 5 (index-of-packet "nppdvjthqldpwncqszvftbrmjlhg")))
  (is (= 6 (index-of-packet* "nppdvjthqldpwncqszvftbrmjlhg")))
  (is (= 9 (index-of-packet "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
  (is (= 10 (index-of-packet* "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
  (is (= 10 (index-of-packet "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))
  (is (= 11 (index-of-packet* "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))))

(deftest index-of-message-by-examples
  (is (= 18 (index-of-message "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
  (is (= 19 (index-of-message* "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
  (is (= 22 (index-of-message "bvwbjplbgvbhsrlpgdmjqwftvncz")))
  (is (= 23 (index-of-message* "bvwbjplbgvbhsrlpgdmjqwftvncz")))
  (is (= 22 (index-of-message "nppdvjthqldpwncqszvftbrmjlhg")))
  (is (= 23 (index-of-message* "nppdvjthqldpwncqszvftbrmjlhg")))
  (is (= 28 (index-of-message "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
  (is (= 29 (index-of-message* "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
  (is (= 25 (index-of-message "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))
  (is (= 26 (index-of-message* "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))))
