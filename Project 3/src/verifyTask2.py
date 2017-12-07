#!/usr/bin/python
from itertools import izip
import subprocess
import sys
import tempfile

def verify(mode, input, expect, language):
    try:
        if language.lower() == 'java':
            p = subprocess.Popen(['java', 'task2', mode, input], stdout=subprocess.PIPE)
            output, error = p.communicate()
        elif language.lower() == 'python':
            p = subprocess.Popen(['python', 'task2.py', mode, input], stdout=subprocess.PIPE)
            output, error = p.communicate()
        elif language.lower() in ['c++', 'cpp', 'c']:
            p = subprocess.Popen(['./task2', mode, input], stdout=subprocess.PIPE)
            output, error = p.communicate()
        else:
            print 'Unsupported language parameter'
        output = output.strip().lower()
        if output == expect:
           print "Passes the test on input: %s, %s, %s" % (
                mode, input, expect)
        else:
            print "Fails the test on input: %s, %s, %s" % (
                mode, input, expect)
    except:
        raise Exception('\nOutput: %s\nError:%s' % (language, output, error))

def verify_batch(language):

    inputs = ["eaab15fbe51a26fe","b9d354baaeec8507","b69efba40bd07ad3","924980e35d518937","8a52a75113a74f6d","7afbc1dfa76731df",
          "ab0b91757ca2cef7","c7d59ba48045ae70","384c640e0837f85b","767ca45d0deac1f7","5bfeeab3a7a8f268","0bd99220a2982aa7",
          "3d9d022aa18afd85","86c86d9e073e1a62","b56dd02afb51c7e3","c75e19d9e902dc1a","5194e3f1370d8583","cebf753bd3983780",
          "67045107ec32f1ce","29cd981f267cec07","0b85f49d7591f4d9","6d672fad5dd96da1","5285e045a792fb85","dc73f8cd9d62c8c8",
          "b5df9ea1fead26c1","430dcd94f76832bf","45a2c73e89434ad0","cb0107578a942ce0","0449f4e923836b98","d5ecd67ac7401ffe",
          "7334fd51ae6204b9","988386b9e6df3d57","767ff18a7a13d392","925845f7f80beab6","3867ab6719e60de9","3e4fb32cfb755829",
          "016ddc08eaadb646","c851e683f1859d31","156246f20d3b19bf","a8d6b6948cfb98f7","98d0ecc4ab58f7bc","8a0decce9e3eefb6",
          "0e7a8f51940ebf40","865e9b64767ffe73","13730837e55e2551","3ead6e8c89ade6a7","f2761c401638d662","e0a2e685dc6dc732",
          "70833498d59dd9ae","f86702bc04cdfe16","3764a2324a9d2a31","43dfb51a67943eab","fb7ff4ae437ad598","a45ef2f83b970ead",
          "3db01c0838c71973","ae19fec4e9e620da","a4049dd008b9e989","68a8791310ab86ef","9e8a2fe5c49858f1","ae3d5254942c31e9",
          "bfaba716ecd675e0","7a9d571670d0b058","d3d5dc52c879a2ba","ba58865829c80d49","3880bf76bf9404e5","0b544379ea023bad",
          "9e3d45a70d2aa4dc","1a79ba57207f6498","160432f1792f91ae","20ea0102a107a13e","cd4c52588a75daea","aeef75e0e568b68c",
          "ec73f29257bcb6fb","681f0e4ce9fee9a2","e02a403b34c7ab2a","58f110ba573702dc","6bf2d69d54387037","3ee561795ef7d5bf",
          "23730164011f32c1","191a522f673d515e","9b6e6ec170a70120","702a19862619e9ce","b55854c792bf5258","0ed979f4c86b61a2",
          "75ba29b93d8fcecd","0e1c6bd0d3cbd008","67a8c134f249c25b","152908a76b19fecb","2f3ecd25c81c160d","0d49ce622c04e085",
          "cdd01fe5a17c5d23","b0d5a413a45defcb","aba4a702ab7a0ef8","404fd9bf4a31fe25","d3f8cb07e6641ccd","a40d40d94ad3ab83",
          "9719bab3c47c9bcd","0880406d7a1cd346","a837ef2c4c8c23a2","dcc1fd79a4f29102",]
    expecteds = ["eaab15fbe51a267f","b9d354baaeec8508","b69efba40bd07a54","924980e35d518938","8a52a75113a74f6e","7afbc1dfa76731e0",
             "ab0b91757ca2cef8","c7d59ba48045aef1","384c640e0837f8dc","767ca45d0deac1f8","5bfeeab3a7a8f2e9","0bd99220a2982aa8",
             "3d9d022aa18afd86","86c86d9e073e1ae3","b56dd02afb51c764","c75e19d9e902dc9b","5194e3f1370d8504","cebf753bd3983701",
             "67045107ec32f14f","29cd981f267cec08","0b85f49d7591f4da","6d672fad5dd96da2","5285e045a792fb86","dc73f8cd9d62c849",
             "b5df9ea1fead26c2","430dcd94f7683240","45a2c73e89434a51","cb0107578a942c61","0449f4e923836b19","d5ecd67ac7401f7f",
             "7334fd51ae6204ba","988386b9e6df3d58","767ff18a7a13d313","925845f7f80bea37","3867ab6719e60dea","3e4fb32cfb75582a",
             "016ddc08eaadb6c7","c851e683f1859d32","156246f20d3b1940","a8d6b6948cfb98f8","98d0ecc4ab58f73d","8a0decce9e3eef37",
             "0e7a8f51940ebfc1","865e9b64767ffef4","13730837e55e2552","3ead6e8c89ade6a8","f2761c401638d6e3","e0a2e685dc6dc7b3",
             "70833498d59dd92f","f86702bc04cdfe97","3764a2324a9d2a32","43dfb51a67943e2c","fb7ff4ae437ad519","a45ef2f83b970eae",
             "3db01c0838c719f4","ae19fec4e9e6205b","a4049dd008b9e98a","68a8791310ab8670","9e8a2fe5c49858f2","ae3d5254942c31ea",
             "bfaba716ecd67561","7a9d571670d0b0d9","d3d5dc52c879a23b","ba58865829c80d4a","3880bf76bf9404e6","0b544379ea023bae",
             "9e3d45a70d2aa45d","1a79ba57207f6419","160432f1792f912f","20ea0102a107a1bf","cd4c52588a75da6b","aeef75e0e568b60d",
             "ec73f29257bcb67c","681f0e4ce9fee923","e02a403b34c7abab","58f110ba5737025d","6bf2d69d54387038","3ee561795ef7d540",
             "23730164011f32c2","191a522f673d51df","9b6e6ec170a701a1","702a19862619e94f","b55854c792bf52d9","0ed979f4c86b6123",
             "75ba29b93d8fcece","0e1c6bd0d3cbd089","67a8c134f249c2dc","152908a76b19fe4c","2f3ecd25c81c160e","0d49ce622c04e086",
             "cdd01fe5a17c5da4","b0d5a413a45def4c","aba4a702ab7a0e79","404fd9bf4a31fe26","d3f8cb07e6641cce","a40d40d94ad3ab04",
             "9719bab3c47c9bce","0880406d7a1cd3c7","a837ef2c4c8c2323","dcc1fd79a4f29183",]




    
    #inputs = ["8080808080808080", "808080808080807f", "8080808080801f80", "8080808080801f7f",
    #         "808080808080b05e", "8080808010101002"]
    #expecteds = ["8080808080808001", "8080808080800180", "8080808080801f01", "8080808080802080",
    #           "808080808080b0df", "8080808010101083"]
    for input, expect in izip(inputs, expecteds):
        verify('enum_key', input, expect, language)

if __name__ == "__main__":
    help_msg = 'Notice: If you use Java or C++, compile them before you run this verifier.'
    print help_msg
    if len(sys.argv[1:]) != 1:
        print 'Wrong number of arguments!\npython verify.py $LANGUAGE'
        sys.exit(1)
    verify_batch(sys.argv[1])