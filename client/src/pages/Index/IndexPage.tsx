import {Test} from 'src/components/Test'
import './Index.scss'

export const Index = () => {
    return (
        <div>
            <h1>
                Приветствуем Вас на коммуникационной платформе
                благотворительного фонда «Национальный фонд помощи медицинским
                учреждениям Фондздрав»!
            </h1>
            <hr className={'mt-16'} />
            <Test />
        </div>
    )
}
