#! /usr/bin/python
#
import tarfile, sys, tempfile, subprocess, os, warnings, shutil
from os.path import isfile, isdir, join, dirname, abspath

class config:
    scanner_public_dir = join(dirname(abspath(sys.argv[0])), "tests/scanner/")
    scanner_hidden_dir = join(dirname(abspath(sys.argv[0])), "tests/scanner/hidden")
    verbose=False

def run_tmpfile(cmd, inputFile):
    '''
    run cmd
    return rv and output
    write input to a temp file so students cant use the filename as a hint
    '''
    f = tempfile.NamedTemporaryFile()
    f.write(inputFile)
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


def diff_answer(testname, correct, students):
    '''check the answer against correct'''
    if correct == students:
        print testname.ljust(10), "CORRECT"
        grade=1
    else:
        print testname.ljust(10), "INCORRECT, DIFFERENCE:"
        grade=0
        if config.verbose:
            #pretty output
            cor=correct.split('\n')
            stu=students.split('\n')
            while len(cor)<len(stu):
                cor.append('')
            while len(cor)>len(stu):
                stu.append('')
            pad = max(map(len, cor))
            print "      |" ,"CORRECT".ljust(pad),"| STUDENT"
            for i in xrange(len(cor)):
                if cor[i]==stu[i]:
                    print "      |",
                else:
                    print "  *** |",
                print cor[i].ljust(pad),'|', stu[i]
    return grade


def testcase_scanner(cmd, testname, inputFile, correctFile):
    '''check if cmd produces the output correct given input'''
    rv, students, tmpfilename = run_tmpfile(cmd, inputFile)
    scannormalize = lambda txt: txt.replace(testname,'').replace(tmpfilename,'').strip()
    return diff_answer(testname, scannormalize(correctFile), scannormalize(students))

def test_scanner(cmd, testdir):
    '''run all tests in testdir'''
    correct=0
    total=0
    for f in sorted(os.listdir(testdir)):
        fi=os.path.join(testdir, f)
        fo=os.path.join(testdir,"output",f+".out")
        if isfile(fi) and isfile(fo):
            correct+=testcase_scanner(cmd, f, open(fi).read(), open(fo).read())
            total+=1
    return correct,total

def main():
    #figure out where Compiler.jar is
    if isfile("./dist/Compiler.jar"):
        cmd=["java", "-jar", "./dist/Compiler.jar"]
    else:
        raise Exception("dist/Compiler.jar is missing")

    #test the scanner
    cmd.extend(["-target","scan"])
    s1 = test_scanner(cmd, config.scanner_public_dir)
    s2 = test_scanner(cmd, config.scanner_hidden_dir)
    scanresult = (s1[0] + s2[0], s1[1] + s2[1])

    #print results
    print
    print "PARSER:   %d of %d correct"%scanresult

if __name__ == "__main__":
    main()


