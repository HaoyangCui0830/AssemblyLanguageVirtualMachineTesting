#!/bin/sh
echo "First re-building to make sure -DNDEBUG is turned on..."
make clean
CFLAGS=-DNDEBUG make
if [ $? -ne 0 ]
then
    echo "Re-building failed"
    exit 1
fi

echo "Running tests that are suppoed to PASS..."
for i in tests/pass/*.s
do
    ./machine-san $i
    if [ $? -ne 0 ]
    then
        echo "Test $i FAILED when it should have PASSED"
        echo ""
        echo "****** FAILURE ******"
        exit 1
    fi
done

echo "Running tests that are suppoed to FAIL..."
for i in tests/fail/*.s
do
    ./machine-san $i
    if [ $? -eq 0 ]
    then
       echo "Test $i PASSED when it should have FAILED"
       echo ""
       echo "****** FAILURE ******"
       exit 1
    fi
done

echo ""
echo "All tests OK"
exit 0
