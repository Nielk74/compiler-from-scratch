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
    rm -f *.lis *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if decac "${f}" 2> "${file}.res" ; then
    	echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.expected") "${file}.lis" > /dev/null ; then
    	echo "--- ${file}: PASSED ---"
    	((nbpassed++))
        else
    	echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
    	echo "IN \"$(cat ${file}.lis)\""
        fi
        echo
    done
fi

if [ -d  "${root}/syntax/valid/${feature}" ]; then
    cd "${root}/syntax/valid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.lis *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if decac "${f}" 2> "${file}.lis" ; then
    	echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.expected") "${file}.lis" > /dev/null ; then
    	echo "--- ${file}: PASSED ---"
    	((nbpassed++))
        else
    	echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
    	echo "IN \"$(cat ${file}.res)\""
        fi
        echo
    done
fi

if [ -d  "${root}/context/invalid/${feature} ]; then
    cd "${root}/context/invalid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.lis *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if decac "${f}" 2> "${file}.lis" ; then
    	echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.expected") "${file}.lis" > /dev/null ; then
    	echo "--- ${file}: PASSED ---"
    	((nbpassed++))
        else
    	echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
    	echo "IN \"$(cat ${file}.lis)\""
        fi
        echo
    done
fi

if [ -d  "${root}/context/valid/${feature}" ]; then
    cd "${root}/context/valid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.lis *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        if decac "${f}" 2> "${file}.lis" ; then
    	echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.expected") "${file}.lis" > /dev/null ; then
    	echo "--- ${file}: PASSED ---"
    	((nbpassed++))
        else
    	echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
    	echo "IN \"$(cat ${file}.lis)\""
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
        decac "${f}" && (ima "${file}.ass" > "${file}.res")
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
        decac "${f}" && (ima "${file}.ass" > "${file}.res")
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

if nbpassed != nbtests then
    exit 1
fi
