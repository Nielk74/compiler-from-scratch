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
        test_context "${f}" > "${file}.res"
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

if [ -d  "${root}/codegen/invalid/${feature}" ]; then
    cd "${root}/codegen/invalid/${feature}"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass *_decompiled.deca || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        # if $2 is set, we want to test the codegen with the given compiler
        if [ "$2" != "" ]; then
            decac -p "${f}" > "${file}_decompiled.deca"
        fi
        if [ -f "${file}.in" ]; then
            if [ "$2" != "" ]; then
            decac "${file}_decompiled.deca" && (cat "${file}.in" | ima "${file}_decompiled.ass" > "${file}_decompiled.res")
            fi
            decac "${f}" && (cat "${file}.in" | ima "${file}.ass" > "${file}.res")
        else
            if [ "$2" != "" ]; then
                decac "${file}_decompiled.deca" && (ima "${file}_decompiled.ass" > "${file}_decompiled.res")
            fi
            decac "${f}" && (ima "${file}.ass" > "${file}.res")
        fi
        if [ -f "${file}.res" ]; then
    	    if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
                if [ "$2" != "" ]; then
                    if [ -f "${file}_decompiled.res" ]; then
    	                if diff -q "${file}_decompiled.res" "${file}.expected" > /dev/null ; then
                            echo "--- ${file}: PASSED ---"
    	                    ((nbpassed++))
                        else
                            echo "--- ${file}: FAILED ---"
                            diff "${file}.expected" "${file}_decompiled.res"
                        fi
                    else
                        echo "--- ${file}: KO ---"
                    fi
                else
    	            echo "--- ${file}: PASSED ---"
    	            ((nbpassed++))
                fi
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
    rm -f *.res *.ass *_decompiled.deca|| exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests++))
        # if $2 is set, we want to test the codegen with the given compiler
        if [ "$2" != "" ]; then
            decac -p "${f}" > "${file}_decompiled.deca"
        fi
        if [ -f "${file}.in" ]; then
            if [ "$2" != "" ]; then
            decac "${file}_decompiled.deca" && (cat "${file}.in" | ima "${file}_decompiled.ass" > "${file}_decompiled.res")
            fi
            decac "${f}" && (cat "${file}.in" | ima "${file}.ass" > "${file}.res")
        else
            if [ "$2" != "" ]; then
                decac "${file}_decompiled.deca" && (ima "${file}_decompiled.ass" > "${file}_decompiled.res")
            fi
            decac "${f}" && (ima "${file}.ass" > "${file}.res")
        fi
        if [ -f "${file}.res" ]; then
    	    if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
                if [ "$2" != "" ]; then
                    if [ -f "${file}_decompiled.res" ]; then
    	                if diff -q "${file}_decompiled.res" "${file}.expected" > /dev/null ; then
                            echo "--- ${file}: PASSED ---"
    	                    ((nbpassed++))
                        else
                            echo "--- ${file}: FAILED ---"
                            diff "${file}.expected" "${file}_decompiled.res"
                        fi
                    else
                        echo "--- ${file}: KO ---"
                    fi
                else
    	            echo "--- ${file}: PASSED ---"
    	            ((nbpassed++))
                fi
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
