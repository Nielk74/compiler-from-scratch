#!/bin/bash

feature = $1
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

cd "${root}/syntax/invalid/"${feature}""
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

cd "${root}/syntax/valid/"${feature}""
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

cd "${root}/context/invalid/"${feature}""
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

cd "${root}/context/valid/"${feature}""
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

cd "${root}/codegen/invalid/"${feature}"""
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

cd "${root}/codegen/valid/"${feature}"""
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


echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"

if nbpassed != nbtests then
    exit 1
fi
