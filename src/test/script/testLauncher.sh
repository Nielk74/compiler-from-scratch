#!/bin/bash

feature=$1
echo "Testing feature "${feature}""
root="$(dirname "$0")/../../.."
decac=""${root}"/src/main/bin/decac"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

root="${root}/src/test/deca"
#echo "### INFO: recompiling java files for $decac ###" 
#mvn compile || exit 1

nbtests=0
nbpassed=0
if [ -d  "${root}/syntax/invalid/${feature}" ]; then
    if [ $(ls -A "${root}/syntax/invalid/${feature}" | wc -l) -ne 0 ]; then
        echo "### TEST: $(pwd) ###"
        rm -f ${root}/syntax/invalid/${feature}/*.{res,ass} || exit 1
        for f in ${root}/syntax/invalid/${feature}/*.deca ; do
            file="${f%.deca}"
            ((nbtests++))
            test_synt "${f}" 2> "${file}.res"
            if grep -q -f "${file}.lis" "${file}.res"
            then
        	echo "--- ${file#*deca/}: PASSED ---"
        	((nbpassed++))
            else
        	echo "--- ${file#*deca/}: FAILED ---"
            fi
            echo
        done
    fi
fi

if [ -d  "${root}/syntax/valid/${feature}" ]; then
    if [ $(ls -A "${root}/syntax/valid/${feature}" | wc -l) -ne 0 ]; then
        echo "### TEST: $(pwd) ###"
        rm -f ${root}/syntax/valid/${feature}/*.{res,.ass} || exit 1
        for f in ${root}/syntax/valid/${feature}*.deca ; do
            file="${f%.deca}"
            ((nbtests++))
            test_synt "${f}" > "${file}.res"
            if [ -f "${file}.res" ]; then
            if diff -q "${file}.res" "${file}.lis" > /dev/null ; then
                echo "--- ${file#*deca/}: PASSED ---"
                ((nbpassed++))
            else
                echo "--- ${file#*deca/}: FAILED ---"
                diff "${file}.lis" "${file}.res" 
            fi
            else
            echo "--- ${file#*deca/}: KO ---"
            fi
            echo
        done
    fi
fi

if [ -d  "${root}/context/invalid/${feature}" ]; then
    if [ $(ls -A "${root}/context/invalid/${feature}" | wc -l) -ne 0 ]; then
        echo "### TEST: $(pwd) ###"
        rm -f ${root}/context/invalid/${feature}/*.{res,ass} || exit 1
        for f in ${root}/context/invalid/${feature}/*.deca ; do
            file="${f%.deca}"
            ((nbtests++))
            decac "${f}" 2> "${file}.res"
            if grep -q -f "${file}.lis" "${file}.res"
            then
        	echo "--- ${file#*deca/}: PASSED ---"
        	((nbpassed++))
            else
        	echo "--- ${file#*deca/}: FAILED ---"
            fi
            echo
        done
    fi
fi

if [ -d  "${root}/context/valid/${feature}" ]; then
    if [ $(ls -A "${root}/context/valid/${feature}" | wc -l) -ne 0 ]; then
        echo "### TEST: $(pwd) ###"
        rm -f ${root}/context/valid/${feature}/*.{res,ass} || exit 1
        for f in ${root}/context/valid/${feature}/*.deca ; do
            file="${f%.deca}"
            ((nbtests++))
            test_context "${f}" > "${file}.res"
            if [ -f "${file}.res" ]; then
            if diff -q "${file}.res" "${file}.lis" > /dev/null ; then
        	echo "--- ${file#*deca/}: PASSED ---"
        	((nbpassed++))
            else
        	echo "--- ${file#*deca/}: FAILED ---"
                        diff "${file}.lis" "${file}.res" 
            fi
            else
            echo "--- ${file#*deca/}: KO ---"
            fi
            echo
        done
    fi
fi

if [ -d  "${root}/codegen/invalid/${feature}" ]; then
    if [ $(ls -A "${root}/codegen/invalid/${feature}" | wc -l) -ne 0 ]; then
        echo "### TEST: $(pwd) ###"
        rm -f ${root}/codegen/invalid/${feature}/*{.res,.ass,_decompiled.deca} || exit 1
        for f in ${root}/codegen/invalid/${feature}/*.deca ; do
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
        	                echo "--- ${file#*deca/}: PASSED ---"
        	                ((nbpassed++))
        	            else
        	                echo "--- ${file#*deca/}: FAILED ---"
        	                diff "${file}.expected" "${file}_decompiled.res"
                            fi
                        else
                            echo "--- ${file#*deca/}: KO ---"
                        fi
                    else
        	            echo "--- ${file#*deca/}: PASSED ---"
        	            ((nbpassed++))
                    fi
        	    else
        	        echo "--- ${file#*deca/}: FAILED ---"
                    diff "${file}.expected" "${file}.res" 
        	    fi
            else
        	    echo "--- ${file#*deca/}: KO ---"
            fi
            echo
        done
    fi
fi

if [ -d  "${root}/codegen/valid/${feature}" ]; then
    if [ $(ls -A "${root}/codegen/valid/${feature}" | wc -l) -ne 0 ]; then
        echo "### TEST: $(pwd) ###"
        rm -f ${root}/codegen/valid/${feature}/*{.res,.ass,_decompiled.deca} || exit 1
        for f in ${root}/codegen/valid/${feature}/*.deca ; do
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
        	                    echo "--- ${file#*deca/}: PASSED ---"
        	                    ((nbpassed++))
        	                else
        	                    echo "--- ${file#*deca/}: FAILED ---"
                                diff "${file}.expected" "${file}_decompiled.res"
                            fi
                        else
                            echo "--- ${file#*deca/}: KO ---"
                        fi
                    else
        	            echo "--- ${file#*deca/}: PASSED ---"
        	            ((nbpassed++))
                    fi
        	    else
        	        echo "--- ${file#*deca/}: FAILED ---"
        	        diff "${file}.expected" "${file}.res" 
        	    fi
            else
        	    echo "--- ${file#*deca/}: KO ---"
            fi
            echo
        done
    fi
fi


echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"

if [ $nbpassed -ne $nbtests ]; then
    exit 1
fi
