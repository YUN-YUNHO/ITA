from pydub import AudioSegment
import os

PATH = './data/'
OUTPUT_FILENAME = PATH + 'output.wav'
file_list = os.listdir(PATH)

# files_path = []
# for _file in file_list:
#     files_path.append(PATH + _file)


audio_seg = []
for _file in file_list:
    audio_seg.append(AudioSegment.from_wav(PATH + _file))

combSound = 0
for sound in audio_seg:
    combSound += sound

try:
    combSound.export(OUTPUT_FILENAME, format="wav")
except Exception as e:
    print(e)

