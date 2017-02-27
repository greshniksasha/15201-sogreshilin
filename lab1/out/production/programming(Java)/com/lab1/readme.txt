                    Lines of Code, app.
------------------------------------------------------------
This utility counts all lines of code in specific directory
    and all its subdirectories.

To use it pass the configuration file path name and
    directory path name as arguments.

In configuration file you can set filters. There are several
    filter available:

- time filter. Counts only files which last modified time
    is greater or less than specified time. To specify the
    time use this syntax in configuration file:
    <(01.01.1970 00:00:00) or >(01.01.1970 00:00:00).

- extension filter. Counts only specific extension files.
    Use this syntax in configuration file:
    .cpp

- agregate filters. You can use AND and OR operations to
    all filters. Suppose that f1, f2 ... fn are the filters.
    Use this syntax in configuration file:
    for AND operation: &(f1 f2 ... fn)
    for OR  operation: |(f1 f2 ... fn)

- negation filter. NOT operation. Suppose that f is a
    filter. Use this syntax in configuration file for NOT
    operation:
    ~(f) or ~f

The result is shown in the table in decreasing order. If
    there are no files satisfy the filter, it will not
    appear in the table. If file satisfies several filters
    it will only be counted once in total number.