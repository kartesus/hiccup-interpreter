(ns hiccup-interpreter.core-test
  (:require [hiccup-interpreter.core :as sut]
            [clojure.test :refer :all]
            [clojure.walk :refer [macroexpand-all]]))

(deftest nil-empty-string
  (is (= "" (sut/eval-hiccup nil)))
  (is (= "" (sut/compile-hiccup nil)))
  (is (= "" (macroexpand-all `(sut/compile-hiccup nil)))))


(deftest string-itself
  (is (= "Hello" (sut/eval-hiccup "Hello")))
  (is (= "Hello" (sut/compile-hiccup "Hello")))
  (is (= "Hello" (macroexpand-all `(sut/compile-hiccup "Hello")))))

(deftest number-string
  (is (= "123" (sut/eval-hiccup 123)))
  (is (= "123" (sut/compile-hiccup 123)))
  (is (= "123" (macroexpand-all `(sut/compile-hiccup 123)))))

(deftest vector-div-html
  (is (= "<div></div>"
         (sut/eval-hiccup [:div])))
  (is (= "<div></div>"
         (sut/compile-hiccup [:div]))))

(deftest vector-span-html
  (is (= "<span></span>"
         (sut/eval-hiccup [:span])))
  (is (= "<span></span>"
         (sut/compile-hiccup [:span]))))

(deftest vector-attribute-html
  (is (= "<a rel=\"link\"></a>"
         (sut/eval-hiccup [:a {:rel "link"}])))
  (is (= "<a rel=\"link\"></a>"
         (sut/compile-hiccup [:a {:rel "link"}]))))

(deftest vector-children-html
  (is (= "<a>helloworld</a>"
         (sut/eval-hiccup [:a "hello" "world"])))
  (is (= "<a>helloworld</a>"
         (sut/compile-hiccup [:a "hello" "world"]))))

(deftest vector-div-div-html
  (is (= "<div><div></div></div>"
         (sut/eval-hiccup [:div [:div]])))
  (is (= "<div><div></div></div>"
         (sut/compile-hiccup [:div [:div]]))))

(deftest vector-list-html
  (is (= "<div><div></div>hello<span>world</span></div>"
         (sut/eval-hiccup
          [:div
           (list [:div]
                 "hello"
                 [:span "world"])])))
  (is (= "<div><div></div>hello<span>world</span></div>"
         (sut/compile-hiccup
          [:div
           (list [:div]
                 "hello"
                 [:span "world"])]))))

(deftest unknown-error
  (is (thrown? clojure.lang.ExceptionInfo
               (sut/eval-hiccup [:div {} "" {}]))))
