from pydub import AudioSegment
import os
import glob
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('--load_path', require=True)
parser.add_argument('--speaker_id', required=True)
parser.add_argument('--script_path', required=True) #합성할 스크립트 대본 경로
parser.add_argument('--sample_path', required=True) #합성본 저장할 경로 (상대경로)
config = parser.parse_args()

PATH = os.getcwd() + config.sample_path #합성본 저장할 경로 (절대경로)

with open(config.script_path, 'rt', encoding='utf-8') as script: 
    for line in script.readlines():
        command = 'python synthesizer.py --load_path ' + config.load_path + ' --num_speaker '+ config.num_speaker + ' --speaker_id ' + config.speaker_id + ' --sample_path ' + PATH + ' --text "' + line
        os.system(command)

file_list = glob.glob(PATH + "\\*.wav")
audio_seg = []
for _file in file_list:
    audio_seg.append(AudioSegment.from_wav(_file))

combSound = 0
for sound in audio_seg:
    combSound += sound

if not os.path.exists(PATH + '\\merge'):
    os.mkdir(PATH + '\\merge')
OUTPUT_FILENAME = PATH + '\\merge\\output.wav'

try:
    combSound.export(OUTPUT_FILENAME, format="wav")
except Exception as e:
    print(e)