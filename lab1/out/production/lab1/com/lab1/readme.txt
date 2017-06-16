
                       Lines of Code
------------------------------------------------------------
This utility counts all lines of code in specific directory
    and all its subdirectories. Configuration file pathname
    and directory pathname are required as arguments. Filters
    can be set in configuration file. There are several
    filters available:

TIME FILTER. Counts only files which last modified time
    is greater or less than specified time. Following syntax
    is used to specify the time (Epoch timestamp):
    <853718400 or >853718400.

EXTENSION FILTER. Counts only specific extension files.
    Syntax in configuration file:
    .cpp

AGREGATE FILTER. AND and OR operations are available to
    all filters. Suppose that {f1, f2 ... fn} are the filters.
    Following syntax is used in configuration file:
    for AND operation: &(f1 f2 ... fn)
    for OR  operation: |(f1 f2 ... fn)

NEGATION FILTER. NOT operation. Suppose that f is a
    filter. Use this syntax in configuration file for NOT
    operation:
    !(f) or !f

The result is shown in the table in decreasing order. If
    there are no files satisfy the filter, it will not
    appear in the table. If file satisfies several filters
    it will only be counted once in total number.

