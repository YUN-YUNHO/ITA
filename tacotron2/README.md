# Tacotron2

## Requirements

- python >= 3.6
- CUDA >= 8.0
- tensorflow >= 1.15


## Data Setting
### Dataset
- KSS : https://www.kaggle.com/bryanpark/korean-single-speaker-speech-dataset
- 아이유
### data location
- wav 파일을 datasets/[화자]/audio 로 이동한 뒤, 
- 텍스트와 audio 파일을 연결해주기 위해 스크립트를 json으로 변환하여 각각 폴더에 넣어줍니다.

- 다음과 같은 구조로 세팅합니다.  
datasets  
&nbsp;&nbsp;&nbsp;&nbsp;└─ 1  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ 1-recognition-All.json  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ audio  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ 1_0000.wav  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ wav files...  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ ...  

### preprocessing
```
python preprocess.py --num_workers 8 --name 1 --in_dir ./datasets/1 --out_dir ./data/1
```
- 폴더들마다 실행해주면 npz 파일이 생성되는데 구조는 'audio', 'melspec', 'linearspec', 'time step', 'mel frames', 'text' ,'tokens', 'loss coeff' 입니다.

## Training
### hyperparameter
- 이미 hparams.py에 설정이 되어있습니다.

### train
- train_tacotron2.py 의 main 함수에서
```
parser.add_argument('--data_paths', default='  ')
```
- default 부분에 json이 들어있는 폴더경로를 입력해줍니다.

- hparams.py 와 train_tacotron2.py 에서 설정을 완료했기 때문에 학습은 다음과 같이 간단합니다.
```
python train_tacotron2.py
```

- 학습을 이어서 진행하는 경우, train_tacotron2.py 에서 --load_path 에 checkpoint 폴더를 넣어줍니다.
```
parser.add_argument('--load_path', default='logdir-tacotron2/1+2+3+4_2020-00-00_00-00-00')
```

- 간결하고 빠른 학습을 위해 vocoder는 별도의 wavenet 을 사용하지 않고 griffin lim 을 사용했습니다.
- 만약 vocoder로 wavenet을 사용하고 싶다면, wavenet을 학습시킨 후 synthesis로 생성된 npy파일을 넣어 결과를 생성할 수 있습니다.

## Synthesis
### principle
```
python synthesizer.py --load_path logdir-tacotron2/${checkpoint folder} --num_speakers ${총 학습인원} --speaker_id ${원하는 목소리(0~)} --text "안녕하세요."
```

### sentence synthesis
콘텐츠를 깔끔하게 음성합성하려면 문장 단위로 합성 후 합쳐야 합니다.
```
python sentence_synthesizer.py --load_path logdir-tacotron2/${checkpoint folder} --num_speakers ${총 학습인원} --speaker_id ${원하는 목소리(0~)} --script_path ${스크립트 경로} --sample_path ${저장할 경로}
```
- 스크립트를 한 줄마다 읽어서 합성 후 생성된 wav 파일들을 하나로 이어 붙입니다.
- sample_path 아래의 merge 폴더에 합쳐진 wav 파일이 생성됩니다.

## From
- [arXiv:1712.05884](https://arxiv.org/abs/1712.05884): Natural TTS Synthesis by Conditioning WaveNet on Mel Spectrogram Predictions
- https://github.com/hccho2/Tacotron2-Wavenet-Korean-TTS







