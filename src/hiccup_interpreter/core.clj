(ns hiccup-interpreter.core
  (:require [clojure.string :as str])
  (:import  [org.apache.commons.text StringEscapeUtils]))

(defn- normalized [input]
  (let [[tag attr? & children] input
        attributes (if (map? attr?) attr? {})
        children   (if (map? attr?) children (cons attr? children))]
    (into [tag attributes] children)))


(defn escape [s]
  (StringEscapeUtils/escapeHtml4 s))

(defn eval-hiccup [input]
  (cond
    (nil? input)
    ""

    (string? input)
    input

    (number? input)
    (str input)

    (vector? input)
    (let [[tag attributes & children] (normalized input)]
      (str "<"
           (name tag)
           (str/join (for [[k v] attributes]
                       (str " " (name k) "=" \" v \")))
           ">"
           (str/join (map eval-hiccup children))
           "</"
           (name tag)
           ">"))

    (seq? input)
    (str/join (map eval-hiccup input))

    :else
    (throw (ex-info "Invalid input" {:input input}))))

(defmacro compile-hiccup [input]
  (cond
    (nil? input)
    ""

    (string? input)
    input

    (number? input)
    (str input)

    :else
    `(eval-hiccup ~input)))
