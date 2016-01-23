#!/usr/bin/python
#
import tarfile, sys, tempfile, subprocess, os, warnings, shutil
from os.path import isfile, isdir, join, dirname, abspath


class config:
    parser_public_dir = join(dirname(abspath(sys.argv[0])), "tests/parser/")
    parser_hidden_dir = join(dirname(abspath(sys.argv[0])), "tests/parser/hidden")
    verbose=True

def run_tmpfile(cmd, i):
    '''
    run cmd
    return rv and output
    write input to a temp file so students cant use the filename as a hint
    '''
    f = tempfile.NamedTemporaryFile()
    f.write(i)
    f.flush()
    try:
        null=open("/dev/null")
    except:
        null=tempfile.TemporaryFile()
    cmd.append(f.name)
    p = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=null)
    cmd.pop()
    output = p.stdout.read()
    rv=p.wait()
    tmpfilename=f.name
    f.close()
    null.close()
    return rv, output, tmpfilename


def testcase_parser(cmd, testname, inputFile):
    '''check if cmd produces the output correct given input'''
    rv, _, _ = run_tmpfile(cmd, inputFile)
    if testname[0:5]=="legal":
        islegal=True
    elif testname[0:7]=="illegal":
        islegal=False
    else:
        return 0

    if islegal == (rv==0):
        print testname.ljust(10), "CORRECT"
        return 1
    else:
        if not config.verbose:
            print testname.ljust(10), "INCORRECT"
        else:
            if islegal:
                print testname.ljust(10), "INCORRECT -- expect exit code 0"
            else:
                print testname.ljust(10), "INCORRECT -- expect exit code other than 0"
        return 0


def test_parser(cmd, testdir):
    '''run all tests in testdir'''
    correct=0
    total=0
    for f in sorted(os.listdir(testdir)):
        fi=os.path.join(testdir, f)
        if isfile(fi):
            correct+=testcase_parser(cmd, f, open(fi).read())
            total+=1
    return correct,total

def main():
    #figure out where Compiler.jar is
    if isfile("./dist/Compiler.jar"):
        cmd=["java", "-jar", "./dist/Compiler.jar"]
    else:
        raise Exception("dist/Compiler.jar is missing")

    #test the parser
    cmd.extend(["-target","parse"])
    s1 = test_parser(cmd, config.parser_public_dir)
    s2 = test_parser(cmd, config.parser_hidden_dir)
    scanresult = (s1[0] + s2[0], s1[1] + s2[1])

    #print results
    print
    print "PARSER:   %d of %d correct"%scanresult

if __name__ == "__main__":
    main()


