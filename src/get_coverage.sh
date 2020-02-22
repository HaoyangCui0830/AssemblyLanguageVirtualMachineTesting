#!/bin/sh

if [ -z ${TOOL_SUFFIX+x} ]
then
    # TOOL_SUFFIX not set
    # try to intelligently set TOOL_SUFFIX
    case $(uname) in
        Linux)
            TOOL_SUFFIX=-6.0
            ;;
        Darwin)
            TOOL_SUFFIX=
            ;;
        *)
            TOOL_SUFFIX=-6.0
    esac
fi


CLANG=clang${TOOL_SUFFIX}
LLVM_PROFDATA=llvm-profdata${TOOL_SUFFIX}
LLVM_COV=llvm-cov${TOOL_SUFFIX}

if [ -z "$(which ${CLANG})" ]
then
    echo "${CLANG} doesn't exist. Try setting TOOL_SUFFIX environment variable"
    exit 1
fi

if [ -z "$(which ${LLVM_PROFDATA})" ]
then
    echo "${LLVM_PROFDATA} doesn't exist. Try setting TOOL_SUFFIX environment variable"
    exit 1
fi

if [ -z "$(which ${LLVM_COV})" ]
then
    echo "${LLVM_COV} doesn't exist. Try setting TOOL_SUFFIX environment variable"
    exit 1
fi

echo "using ${CLANG}, ${LLVM_PROFDATA} and ${LLVM_COV}"

export LLVM_PROFILE_FILE="machine-%m.profraw"

if [ $# -lt 2 ]
then
    echo "Usage: $0 cycles inputfile1 [inputfile2 ...]"
    exit 1
fi

CYCLES=$1
shift 1

rm -f machine*.profraw machine.profdata

echo "First re-building to make sure -DNDEBUG is turned on..."
rm -f machine-cov
CLANG=${CLANG} CFLAGS=-DNDEBUG make machine-cov

while [ ! -z "$1" ]
do
    ./machine-cov $1 $CYCLES
    shift 1
done

${LLVM_PROFDATA} merge -sparse machine*.profraw -o machine.profdata
${LLVM_COV} show ./machine-cov -instr-profile=machine.profdata
${LLVM_COV} report ./machine-cov -instr-profile=machine.profdata
