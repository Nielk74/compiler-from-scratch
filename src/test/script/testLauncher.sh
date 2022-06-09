#!/bin/bash

feature=$1
echo "Testing feature "${feature}""

cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac=""${root}"/src/main/bin/decac"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

echo "### INFO: recompiling java files for $decac ###" 
cd "../../.."
mvn compile || exit 1

nbtests=0
nbpassed=0
if [ -d  "${root}/syntax/invalid/${feature}" ]; then
    cd "${root}/syntax/invalid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        test_synt "${f}" 2> "${file}.res"
        if grep -q -f "${file}.lis" "${file}.res"
        then
            echo "--- ${file}: PASSED ---"
            ((nbpassed++))
        else
            echo "--- ${file}: FAILED ---"
        fi
        echo
    done
fi

if [ -d  "${root}/syntax/valid/${feature}" ]; then
    cd "${root}/syntax/valid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        test_synt "${f}" > "${file}.res"
        if [ -f "${file}.res" ]; then
        if diff -q "${file}.res" "${file}.lis" > /dev/null ; then
            echo "--- ${file}: PASSED ---"
            ((nbpassed++))
        else
            echo "--- ${file}: FAILED ---"
            diff "${file}.lis" "${file}.res" 
        fi
        else
        echo "--- ${file}: KO ---"
        fi
        echo
    done
fi

if [ -d  "${root}/context/invalid/${feature}" ]; then
    cd "${root}/context/invalid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if decac "${f}" 2> "${file}.res" ; then
    	echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.lis") "${file}.res" > /dev/null ; then
    	echo "--- ${file}: PASSED ---"
    	((nbpassed++))
        else
    	echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.lis)\""
    	echo "IN \"$(cat ${file}.res)\""
        fi
        echo
    done
fi

if [ -d  "${root}/context/valid/${feature}" ]; then
    cd "${root}/context/valid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if test_context "${f}" > "${file}.res" ; then
    	echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.lis") "${file}.res" > /dev/null ; then
    	echo "--- ${file}: PASSED ---"
    	((nbpassed++))
        else
    	echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.lis)\""
    	echo "IN \"$(cat ${file}.res)\""
        fi
        echo
    done
fi

if [ -d  "${root}/codegen/invalid/${feature}" ]; then
    cd "${root}/codegen/invalid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if [ -f "${file}.in" ]; then
            decac "${f}" && (cat "${file}.in" | ima "${file}.ass" > "${file}.res")
        else
            decac "${f}" && (ima "${file}.ass" > "${file}.res")
        fi
        if [ -f "${file}.res" ]; then
    	if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
    	    echo "--- ${file}: PASSED ---"
    	    ((nbpassed++))
    	else
    	    echo "--- ${file}: FAILED ---"
    	    diff "${file}.expected" "${file}.res" 
    	fi
        else
    	echo "--- ${file}: KO ---"
        fi
        echo
    done
fi

if [ -d  "${root}/codegen/valid/${feature}" ]; then
    cd "${root}/codegen/valid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if [ -f "${file}.in" ]; then
            decac "${f}" && (cat "${file}.in" | ima "${file}.ass" > "${file}.res")
        else
            decac "${f}" && (ima "${file}.ass" > "${file}.res")
        fi
        if [ -f "${file}.res" ]; then
    	if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
    	    echo "--- ${file}: PASSED ---"
    	    ((nbpassed++))
    	else
    	    echo "--- ${file}: FAILED ---"
    	    diff "${file}.expected" "${file}.res" 
    	fi
        else
    	echo "--- ${file}: KO ---"
        fi
        echo
    done
fi


echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"

if [ $nbpassed -ne $nbtests ]; then
    exit 1
fi
